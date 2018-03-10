export class Location {
    name: string;
    description: string;
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

export const locations: Location[] = [];