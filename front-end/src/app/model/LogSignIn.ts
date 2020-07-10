export class LogSignIn {
  id:number;
  userID : number;
  ip : string;
  browser : string;
  browserVersion : string;
  device : string;
  os : string;
  osVersion : string;
  dateTime : string;

  constructor(id: number, userID: number, ip: string, browser: string, browserVersion: string, device: string, os: string, osVersion: string, dateTime: string) {
    this.id = id;
    this.userID = userID;
    this.ip = ip;
    this.browser = browser;
    this.browserVersion = browserVersion;
    this.device = device;
    this.os = os;
    this.osVersion = osVersion;
    this.dateTime = dateTime;
  }
}
