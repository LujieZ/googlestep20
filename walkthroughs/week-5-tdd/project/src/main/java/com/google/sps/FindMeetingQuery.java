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

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public final class FindMeetingQuery {
    public static final int DAY_START = TimeRange.START_OF_DAY;
    public static final int DAY_END = TimeRange.END_OF_DAY;
    public static final TimeRange WHOLE_DAY = TimeRange.WHOLE_DAY;

    /**
      * Find all times that the meeting could happen. Two pointers rangeStart
      * and rangeEnd are pointing at the current avaliable TimeRange. If the
      * current avaliable TimeRange has longer duration than the meeting duration,
      * add it to avaliable meetingTimes.
      */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();
        long duration = request.getDuration();
        Collection<String> attendees = request.getAttendees(); 

        // If the meeting duration is longer than the whole day TimeRange,
        // return empty array as the meetingTimes collection.
        if (duration > WHOLE_DAY.duration()) {
            return Arrays.asList();
        }

        // Remove double booked attendees for the meeting.
        Set<String> attendeesSet = new HashSet<String>();
        for (String attendee : attendees) {
            attendeesSet.add(attendee);
        }

        // The TimeRange of the last event that is went through.
        TimeRange lastRange = TimeRange.fromStartDuration(DAY_END, 0);
        int rangeStart = DAY_START; // Pointer to start of the previous event.
        int rangeEnd = DAY_END; // Pointer to the end of the previous event.

        // Linear search the current events to add new TimeRange to meetingTimes.
        for (Event event : events) {
            Collection<String> eventAttendees = event.getAttendees();
            TimeRange eventWhen = event.getWhen();
            int eventStart = eventWhen.start();
            int eventEnd = eventWhen.end();

            // Check if the pervious event and current event overlap.
            if (rangeStart > eventStart){
                // If overlap, change the next avaliable rangeTime to the later
                // end time of the two.
                if (eventEnd > lastRange.end()) {
                    rangeStart = eventEnd;
                } else {
                    rangeStart = lastRange.end();
                }
            // If they don't overlap, check if required attendees shows up in
            // the event.
            } else {
                // If not, set the whole day as avaliable.
                if (!attendeesSet.containsAll(eventAttendees)) {
                        return Arrays.asList(WHOLE_DAY);
                }
                TimeRange newRange = TimeRange.fromStartEnd(rangeStart, eventStart, false);
                if (newRange.duration() >= duration) {
                    meetingTimes.add(newRange);
                }
                rangeStart = eventEnd;
            }
            lastRange = eventWhen;
        }
        TimeRange finalRange = TimeRange.fromStartEnd(rangeStart, rangeEnd, true);
        
        // Check if the duration of finalRange longer than the meeting duration.
        if (finalRange.duration() > duration) {
            meetingTimes.add(finalRange);
        }
        return meetingTimes;
    }


}
