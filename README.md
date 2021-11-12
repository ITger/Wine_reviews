# Wine_reviews
wine reviews with variety, location, winery, price, and description

Sample project on how to parse a large json file, 
how to use composite predicates on java stream.
All the dataset resides in-memory.

The dataset was taken from https://www.kaggle.com/zynicide/wine-reviews#winemag-data-130k-v2.json
To use this sample project you must download the winemag-data-130k-v2.json to the src/main/resources/ folder!

<s>This sample is running on Google cloud: http://wine-resource.appspot.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config </s>

Sample queries:

<b>The following endpoints are no longer accessible!</b>

curl -X GET ~~"http://wine-resource.appspot.com/api/WineGlass/count"~~ -H "accept: */*"

curl -X GET ~~"http://wine-resource.appspot.com/api/WineGlass/available_fields"~~ -H "accept: */*"

curl -X POST ~~"http://wine-resource.appspot.com/api/WineGlass/wineSelection"~~ -H "accept: application/json" -H "Content-Type: application/json" -d "{\"wineSelection\":{\"description\":[\"blackberry finish\",\"chocolate\",\"tannic\"],\"country\":[\"Argentina\"]}}"

curl -X POST ~~"http://wine-resource.appspot.com/api/WineGlass/wineSelection"~~ -H "accept: application/json" -H "Content-Type: application/json" -d "{\"wineSelection\":{\"country\":[\"Spain\"],\"description\":[\"spicy\",\"berry\",\"herbal\"]}}"

curl -X POST ~~"http://wine-resource.appspot.com/api/WineGlass/wineSelection"~~ -H "accept: application/json" -H "Content-Type: application/json" -d "{\"wineSelection\":{\"description\":[\"tomato\",\"fresh\",\"juicy\"]}}"

Details here: https://zerynger.wordpress.com/2020/03/19/wine-reviews-with-java-stream-api-and-google-cloud/


