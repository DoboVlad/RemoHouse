export class GSMController {
  id : number;
  roomID : number;
  phoneNumber : string;
  status : string;

  constructor(id: number, roomID: number, phoneNumber: string, status: string) {
    this.id = id;
    this.roomID = roomID;
    this.phoneNumber = phoneNumber;
    this.status = status;
  }

}
