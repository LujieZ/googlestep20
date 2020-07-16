// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
    public static final int DAY_START = TimeRange.START_OF_DAY;
    public static final int DAY_END = TimeRange.END_OF_DAY;
    public static final TimeRange WHOLE_DAY = TimeRange.WHOLE_DAY;

    /**
      * Return a collection of TimeRanges that attendees can meet for a requested 
      * meeting. If there are no TimeRanges that works for both required and optional 
      * attendees, find TimeRanges that just work for required attendees. If there 
      * are no required attendees, find the TimeRanges for just the optional attendees.
      */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        long meetingDuration = request.getDuration();
        Collection<String> requiredAttendees = request.getAttendees(); 
        Collection<String> allAttendees = new ArrayList<>(requiredAttendees);
        allAttendees.addAll(request.getOptionalAttendees());

        if (meetingDuration > WHOLE_DAY.duration() || meetingDuration <= 0) {
            // The meeting duation is invalid.
            return Arrays.asList();
        }

        // Find TimeRanges that both required and optional attendees are available.
        Collection<TimeRange> allMeetingTimes = 
            findMeetingTimes(events, allAttendees, meetingDuration);

        if (!allMeetingTimes.isEmpty()) {
            return allMeetingTimes;
        }

        // If there is no required attendee, return allMeetingTimes for 
        // optional attendees.
        if (requiredAttendees.isEmpty()) {
            return allMeetingTimes;
        }

        // If not, return available TimeRange for required attendees.
        return findMeetingTimes(
            events, requiredAttendees, meetingDuration);
    }

    /**
      * Find all times that the meeting could happen for given attendees. Loop through 
      * the given events and update the rangeStart pointer, so that it is pointing at the 
      * start of the current available TimeRange. If the current available TimeRange has 
      * longer duration than the requested meeting duration, add it to available meetingTimes. 
      * Then update the pointer to the end of the current event.
      */
    private static Collection<TimeRange> findMeetingTimes(
        Collection<Event> events, Collection<String> attendees, long duration) {
        Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();

        // Sort the events by the start time in acsending order.
        List<Event> eventsSorted = new ArrayList<Event>(events);
        Collections.sort(eventsSorted, Event.SORT_BY_START_ASCENDING);

        // Candidate for the start of the current available TimeRange.
        // It will also be pointing at the end of previous event that 
        // contains any of the give attendees.
        int rangeStart = DAY_START;

        // Loop through the sorted events and add new TimeRange to 
        // meetingTimes.
        for (Event event : eventsSorted) {
            Collection<String> eventAttendees = event.getAttendees();
            TimeRange eventWhen = event.getWhen();
            int eventStart = eventWhen.start();
            int eventEnd = eventWhen.end();

            // Check if given attendees shows up in the event.
            if (isAttending(attendees, eventAttendees)) {
                if (rangeStart >= eventStart) {
                    // There is overlap or no gap between the events.
                    if (eventEnd > rangeStart) {
                        // Update the pointer so that it is pointing at the start of new 
                        // available TimeRange.
                        rangeStart = eventEnd;
                    }
                } else {
                    // If they are separate, initialize the current available TimeRange.            
                    TimeRange newRange = TimeRange.fromStartEnd(
                            rangeStart, eventStart, false);
                        if (newRange.duration() >= duration) {
                            meetingTimes.add(newRange);
                        }
                    rangeStart = eventEnd;
                }
            }
        }

        TimeRange finalRange = TimeRange.fromStartEnd(rangeStart, DAY_END, true);
        if (finalRange.duration() >= duration) {
            meetingTimes.add(finalRange);
        }

        return meetingTimes;
    }

    /**
      * Check and return true if any given attendees present in the eventAttendees. 
      * Return false otherwise.
      */
    private static boolean isAttending(
        Collection<String> attendees, Collection<String> eventAttendees) {
        for (String eventAttendee : eventAttendees) {
            if (attendees.contains(eventAttendee)) {
                return true;
            }
        }
        return false;
    }
}
