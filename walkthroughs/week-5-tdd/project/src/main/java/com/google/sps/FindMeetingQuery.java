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

    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        long meetingDuration = request.getDuration();
        // Required attendees for the request meeting.
        Collection<String> requiredAttendees = request.getAttendees(); 
        // All attendees (required and optional) for the request meeting.
        Collection<String> allAttendees = new ArrayList<>(requiredAttendees);
        allAttendees.addAll(request.getOptionalAttendees());

        // If the meeting duration is invalid, return an empty array list as 
        // the meetingTimes collection.
        if (meetingDuration > WHOLE_DAY.duration() || meetingDuration == 0) {
            return Arrays.asList();
        }

        // Find available TimeRange for both required and optional attendees.
        Collection<TimeRange> allMeetingTimes = 
            findMeetingTimes(events, allAttendees, meetingDuration);

        // If there is meeting time for all, or there is no required attendee, 
        // return meetingTimes for all attendees.
        if (allMeetingTimes.size() > 0 || requiredAttendees.size() == 0) {
            return allMeetingTimes;
        }
        // If not, return available TimeRange for required attendees.
        return findMeetingTimes(
            events, requiredAttendees, meetingDuration);
    }

    /**
      * Find all times that the meeting could happen, given attendees and duration. 
      * Pointer rangeStart is pointing at the start of the current available 
      * TimeRange. If the current available TimeRange has longer duration than the 
      * requested meeting duration, add it to available meetingTimes.
      */
    private static Collection<TimeRange> findMeetingTimes(
        Collection<Event> events, Collection<String> attendees, long duration) {
        Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();

        // Sort the events by the start time.
        List<Event> eventsSorted = new ArrayList<Event>(events);
        Collections.sort(eventsSorted, Event.SORT_BY_START_TIME);

        int rangeStart = DAY_START; // Pointer to start of the previous event.

        // Linear search the current events and add new TimeRange to 
        // meetingTimes.
        for (Event event : eventsSorted) {
            Collection<String> eventAttendees = event.getAttendees();
            TimeRange eventWhen = event.getWhen();
            int eventStart = eventWhen.start();
            int eventEnd = eventWhen.end();

            // Check if given attendees shows up in the event.
            if (attendees.containsAll(eventAttendees)) {
                // Check if the current event starts before the previous event ends,
                // or there is no gap in between.
                if (rangeStart >= eventStart) {
                    // If so, check which ends earilier and change the start of next 
                    // available TimeRange to the later end time of the two.
                    if (eventEnd > rangeStart) {
                        rangeStart = eventEnd;
                    }
                // If they don't overlap, initialize the current available TimeRange.
                } else {               
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
        
        // Check if the duration of the final TimeRange longer than the meeting 
        // duration.
        if (finalRange.duration() >= duration) {
            meetingTimes.add(finalRange);
        }
        return meetingTimes;
    }
}
