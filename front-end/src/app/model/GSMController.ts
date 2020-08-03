export class GSMController {
  id: number;
  roomID: number;
  phoneNumber: string;
  status: string;
  type: string;
  name: string;


  constructor(id: number, roomID: number, phoneNumber: string, status: string, type: string, name: string) {
    this.id = id;
    this.roomID = roomID;
    this.phoneNumber = phoneNumber;
    this.status = status;
    this.type = type;
    this.name = name;
  }
}
