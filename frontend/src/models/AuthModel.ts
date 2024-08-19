export class LoginModel {
  email!: string;
  password!: string;

  constructor() {
    this.email = "";
    this.password = "";
  }
}
export interface Address {
  street_name: string;
  suite_number: string;
  pincode: string;
  city: string;
  state: string;
  country: string;
  // postalCode:string;
}

export class CreateUserModel {
  id?: number;
  firstName!: string;
  lastName!: string;
  email!: string;
  address!: Address;
  password!: string;
  phone!: string;
  birthDate!: string;
  confirmPassword?: string;
}
export class TokenModel {
  accessToken!: string;
  refreshToken!: string;
}
export class LogOutModel {
  refreshToken!: string;
}
