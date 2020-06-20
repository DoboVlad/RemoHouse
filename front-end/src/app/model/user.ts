export class User {
  id: number;
  name: string;
  surname : string;
  phoneNumber : string;
  password : string;
  email : string;

  constructor(id: number, name: string, surname: string, phoneNumber: string, password: string, email: string) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.email = email;
  }
}
