export class Location {
    id: string;
    name: string;
    description: string;
    geoLocation: Point;
    menus: Menu[];

    constructor(id: string, name: string, description: string, geoLocation: Point, menus: Menu[]) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.geoLocation = geoLocation;
        this.menus = menus;
    }
}

export class Point {
    latitude: number;
    longitude: number;
    distance: number;

    constructor(latitude: number, longitude: number, distance?: number) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;

    }
}

export class Menu {
    title: string;
    description: string;
    price: number;
    variants: string;

    constructor(title: string, description: string, price: number, variants: string) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.variants = variants;

    }
}

export class LocationFactory {
    static empty(): Location {
        return new Location('', '', '', new Point(0, 0), new Array<Menu>());
    }

    static fromObject(rawLocation: any): Location {
        return new Location(
            rawLocation.id,
            rawLocation.name,
            rawLocation.description,
            rawLocation.geoLocation,
            rawLocation.menus
        );
    }
}
