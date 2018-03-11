db.location.drop();
db.location.insert({
    "name" : "Hungry Belly",
    "description" : "Irgendwo in Altona",
    "menuUrls" : [],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.567544, 
            9.93273
        ]
    }
});
db.location.insert({
    "name" : "Chili Club",
    "description" : "Am historischen Hafen",
    "menuUrls" : [],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.542928, 
            9.993006
        ]
    }
});
db.location.insert({
    "name" : "Feinkost Hafencity",
    "description" : "Alias Nudelthomas",
    "menuUrls" : ["http://feinkosthafencity.de/"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.5423283, 
            9.9887728
        ]
    }
});
db.location.insert({
    "name" : "Maredo",
    "description" : "MAREDO - das ist Leidenschaft für beste Steaks, Erstklassige Küche und genussvolles Miteinander.",
    "menuUrls" : ["https://www.maredo.de/speisekarte/steaks/", "https://www.maredo.de/speisekarte/salate/", "https://www.maredo.de/speisekarte/vorspeisen-suppen/", "https://www.maredo.de/speisekarte/klassiker/", "https://www.maredo.de/speisekarte/burger/"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.542678,
            9.994692
        ]
    }
});
db.location.insert({
    "name" : "India House",
    "description" : "Unser Restaurant India House heißt Sie willkommen. Wir sind ein indisches Restaurant, das Ihnen indische Speisen in großer Auswahl präsentiert. Unser Restaurant in Hamburg möchte, dass Sie sich kulinarisch auf eine Reise begeben und unsere leckeren und hausgemachten Gerichte genießen. Unser indisches Restaurant überzeugt mit einem stimmungsvollen und stilvollen Ambiente und empfängt Sie mit indischer Gastfreundlichkeit. In unserem Restaurant in Hamburg können Sie indische Spezialitäten genießen. Dabei legen wir großen Wert auf unseren Service sowie auf gute und frische Zutaten.",
    "menuUrls" : ["http://www.restaurant-india-house-hamburg.de/speisen/"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.541719,
            9.987675
        ]
    }
});
db.location.insert({
    "name" : "Alt Hamburger Aalspeicher",
    "description" : "Das urige Restaurant mit Terrasse und Blick auf das Nikolaifleet serviert Fischspezialitäten wie Räucheraal.",
    "menuUrls" : ["https://www.aalspeicher.de/"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.545652, 9.987212]
    }
});
db.location.insert({
    "name" : "Mamson",
    "description" : "",
    "menuUrls" : [],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.543318, 9.997799]
    }
});
db.location.insert({
    "name" : "Andronaco",
    "description" : "",
    "menuUrls" : ["http://www.andronaco.info/standorte/hamburg-hafencity/bistro-speisekarte/"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.543918, 9.997198]
    }
});
db.location.insert({
    "name" : "Wandrahm",
    "description" : "",
    "menuUrls" : [],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.541689, 9.991328]
    }
});
db.location.insert({
    "name" : "green lovers",
    "description" : "",
    "menuUrls" : ["https://www.greenlovers.de/essen/salate-bowls"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.541667, 9.986516]
    }
});
db.location.insert({
    "name" : "Ciao Mamma!",
    "description" : "",
    "menuUrls" : ["http://www.ciaomamma.de/index.php/menu"],
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [53.543078, 9.985291]
    },
    "menuParserConfig" : {
    	"parsePreviousElement" : true,
    }
});
db.location.insert({
	"name" : "Oh it's fresh",
	"description" : "",
	"menuUrls" : [],
	"geoLocation" : {
		"type" : "Point",
		"coordinates" : [53.542909, 9.984944]
	}
});
db.location.createIndex( { geoLocation : "2dsphere" } );