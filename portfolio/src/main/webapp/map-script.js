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

var map;
/**
 * Initial the map and add it to the map page.
 */
function initMap() {
    var mapCenter = { lat: 22.319154, lng: 9.275316 }
    var myHometown  = { lat: 34.724384, lng: 113.640486 }
    var myHighschool = { lat: 41.342082, lng: -72.913567 }
    var myLocation = { lat: 40.678038, lng: -73.950493 }
    var winterTravel = { lat: 34.050925, lng: -118.243498 }
    var summerTravel = { lat: 31.208968, lng: 121.468941 }

    map = new google.maps.Map(
        document.getElementById('map'), 
        {center: mapCenter,
        zoom: 2.1,
        styles: [ // Style the map with dark mode.
            {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
            {
              featureType: 'administrative.locality',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'geometry',
              stylers: [{color: '#263c3f'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'labels.text.fill',
              stylers: [{color: '#6b9a76'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry',
              stylers: [{color: '#38414e'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry.stroke',
              stylers: [{color: '#212a37'}]
            },
            {
              featureType: 'road',
              elementType: 'labels.text.fill',
              stylers: [{color: '#9ca5b3'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry',
              stylers: [{color: '#746855'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry.stroke',
              stylers: [{color: '#1f2835'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'labels.text.fill',
              stylers: [{color: '#f3d19c'}]
            },
            {
              featureType: 'transit',
              elementType: 'geometry',
              stylers: [{color: '#2f3948'}]
            },
            {
              featureType: 'transit.station',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'water',
              elementType: 'geometry',
              stylers: [{color: '#17263c'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.fill',
              stylers: [{color: '#515c6d'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.stroke',
              stylers: [{color: '#17263c'}]
            }
          ]
    });

    const hometownMarker = new google.maps.Marker({
        position: myHometown,
        map: map,
        title: 'This is where I grew up.'
    });
    const highschoolMarker = new google.maps.Marker({
        position: myHighschool,
        map: map,
        title: 'I went to highschool here.'
    });
    const locationMarker = new google.maps.Marker({
        position: myLocation,
        map: map,
        title: 'I live in this place now.'
    });
    const winterTravelMarker = new google.maps.Marker({
        position: winterTravel,
        map: map,
        title: 'I travelled here last winter.'
    });
    const summerTravelMarker = new google.maps.Marker({
        position: summerTravel,
        map: map,
        title: 'I go here a lot during summmer.'
    });
}
