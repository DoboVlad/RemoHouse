export class ActionLogGSM {
  id : number;
  operationType : string;
  dateTime : string;
  userID : number;
  gsmControllerID : number;
  gsmControllerType : string;
  roomName : string;
  locationName : string;

  constructor(id: number, operationType: string, dateTime: string, userID: number, gsmControllerID: number, gsmControllerType: string, roomName: string, locationName: string) {
    this.id = id;
    this.operationType = operationType;
    this.dateTime = dateTime;
    this.userID = userID;
    this.gsmControllerID = gsmControllerID;
    this.gsmControllerType = gsmControllerType;
    this.roomName = roomName;
    this.locationName = locationName;
  }
}
