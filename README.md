# Wine_reviews
wine reviews with variety, location, winery, price, and description

Sample project on how to parse a large json file, 
how to use composite predicates on java stream.
All the dataset resides in-memory.

The dataset was taken from https://www.kaggle.com/zynicide/wine-reviews#winemag-data-130k-v2.json
To use this sample project you must download the winemag-data-130k-v2.json to the src/main/resources/ folder!

This sample is running on Google cloud:

http://wine-resource.appspot.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

Sample queries:

curl -X GET "http://wine-resource.appspot.com/api/WineGlass/count" -H "accept: */*"

curl -X GET "http://wine-resource.appspot.com/api/WineGlass/available_fields" -H "accept: */*"

curl -X GET "http://wine-resource.appspot.com/api/WineGlass/wineSelection?country=Spain&description=horseradish&variety=Tempranillo" -H "accept: */*"

curl -X GET "http://wine-resource.appspot.com/api/WineGlass/wineSelection?country=Spain&variety=Red%20Blend&description=aromas%20of%20chewing%20gum" -H "accept: */*"

Details (in Polish) here: https://zerynger.wordpress.com/2020/03/19/wino-i-stream-api/


