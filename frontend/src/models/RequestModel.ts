export default class RequestModel {
  category!: string;
  urgency!: string;
  description!: string;
  location!: string;
  date!: string;
  time!: string;
  duration!: number;
  status!: string;
  requestStatus!: "OPEN";
}

export  class FetchRequestModel {
  requestId!: number;
  userId!:number;
  requestCategory!: string;
  urgency!: string;
  description!: string;
  location!: string;
  date!: string;
  time!: string;
  duration!: number;
  status!: string;
  requestStatus!: "OPEN";
}
