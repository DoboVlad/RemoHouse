export class Room {
  NAME_REGEX : string = "^[A-Za-z][A-Za-z0-9]*$";
  id : number;
  locationID : number;
  name : string;

  constructor(id: number, locationID: number, name: string) {
    this.id = id;
    this.locationID = locationID;
    this.name = name;
  }
}
