export class Location {
  LAT_LONG_REGEX : string = "^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$";
  id : number;
  latitude : string;
  longitude : string;
  image : string;
  name : string;
  userID : number;

  constructor(id: number, latitude: string, longitude: string, image: string, name: string, userID: number) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.image = image;
    this.name = name;
    this.userID = userID;
  }
}
