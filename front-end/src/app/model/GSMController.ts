export class GSMController {
  id : number;
  roomID : number;
  phoneNumber : string;
  status : string;
  type : string;


  constructor(id: number, roomID: number, phoneNumber: string, status: string, type: string) {
    this.id = id;
    this.roomID = roomID;
    this.phoneNumber = phoneNumber;
    this.status = status;
    this.type = type;
  }
}
