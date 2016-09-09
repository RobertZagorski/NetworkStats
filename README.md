## TrafficStats vs. NetworkStatsManager ##

Android M introduced new package for monitoring data usage statistics for the device or package.

    android.app.usage.NetworkStatsManager
    
This project is a demonstration of the capabilities of the new model. A sample app shows the correct
way to obtain all required permissions and how they should be called in order to get the data.

The app view shows comparison between old way ([`TrafficStats`](https://developer.android.com/reference/android/net/TrafficStats.html))
and a new way ([`NetworkStatsManager`](https://developer.android.com/reference/android/app/usage/NetworkStatsManager.html))