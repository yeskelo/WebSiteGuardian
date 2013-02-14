WebSiteGuardian
===============

Learning task 2

Description:

This app will conduct periodical check if target web site is accessible (returns 200 OK). It has Preferences Activity (website url), service on backgorund is running periodical job to kick httpclient and see if website is ON. Results of each check has to be stored in local DB. Main activity shows last result with timestamp and has button to run/stop the service. At this point httpclient has to be mocked and have to return 200 or 500 http codes with equal probability. 
