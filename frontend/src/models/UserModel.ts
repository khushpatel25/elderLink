export default class UserModel {
  id?: number;
  firstName!: string;
  lastName!: string;
  email!: string;
  address!: Address;
  password!: string;
  phone!: string;
  birthDate!: string;
  userType: string | undefined;
  creditBalance: string | undefined;
}

export interface Address {
  street_name: string;
  suite_number: string;
  pincode: string;
  city: string;
  state: string;
  country: string;
}
