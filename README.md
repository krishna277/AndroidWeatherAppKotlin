# Android WeatherApp [Kotlin]

Description:
* Displays 5 day weather forecast for Singapore city using API from https://openweathermap.org/forecast5.


Building:
* Get your own appid from https://home.openweathermap.org/users/sign_up and use it for API_ID definition in WeatherClient.kt file.
* Build the app using Android Studio.


Running the App:
* When the "Singapore Weather" App is launched, a circular progress bar will be shown during the loading of data from openwethermap.
* Once the data is loaded from openweathermap, 5 weather forecast (includes weather data every 3hours) will be displayed.
* If the view is pulled down and released, the weather data will be loaded again from openweathermap.
* SQLite database is used to keep backup of forecast data from network.
* If can't get data from network, the data will be loaded from SQLite local database in the device.
* If failure to get data from both network and database, an error message box will be displayed with retry option.


Testing:
* Few JUNIT tests are provided to showcase unit testing for Android Testing and module testing.



Improvements that can be done:
* Support search for different cities:
Option1: Download city list data from http://bulk.openweathermap.org/sample/city.list.json.gz
and store in SQLite database. Use SQL query to find the right city id to use with OpenWeatherMap API.

Option2: Setup search server as described in https://gitlab.com/mvysny/owm-city-finder

* Use advanced features like Retrofit , Rx , Room to improve responsiveness. Currently, just used basic apis.

* Add more test cases for good coverage.






