export class ActionLogGSM {
  id : number;
  operationType : string;
  dateTime : string;
  userID : number;
  gsmControllerID : number;

  constructor(id: number, operationType: string, dateTime: string, userID: number, gsmControllerID: number) {
    this.id = id;
    this.operationType = operationType;
    this.dateTime = dateTime;
    this.userID = userID;
    this.gsmControllerID = gsmControllerID;
  }
}
