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

import java.lang.String;

/**
 * Utility class for creating greeting messages.
 */
public class Greeter {
    /**
     * Returns a greeting for the given name.
     */
    public String greet(String name) {
        // Trim white spaces in the name.
        name = name.replaceAll("\\s+", "");
        // Remove special characters: @,#,$,%.
        String actualName = "";
        for (int i = 0; i < name.length(); i++) {
            char letter = name.charAt(i);
            if (letter != '@' && letter != '#' && letter != '$' && letter != '%') {
                actualName += letter;
            }
        }
        return "Hello " + actualName;
    }
}
