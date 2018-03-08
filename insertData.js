db.location.drop();
db.location.insert({
    "name" : "Hungry Belly",
    "description" : "Irgendwo in Altona",
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
    "geoLocation" : {
        "type" : "Point",
        "coordinates" : [ 
            53.5423283, 
            9.9887728
        ]
    }
});
db.location.createIndex( { geoLocation : "2dsphere" } );