export class Location {
    name: string;
    description: string;
}

export class Point {
    latitude: number;
    longitude: number;

    constructor(latitude: number, longitude: number) {
        this.latitude = latitude;
        this.longitude = longitude;

    }
}

export const locations: Location[] = [];