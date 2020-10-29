export interface Values {
  timestamp: number;
  value: number;
}

export interface ValuesRange {
  minTimestamp: number;
  maxTimestamp: number;
}

export interface ValuesResponse {
  requestId: string;
  isSuccess: boolean;
  errorMessage: string;
  values: Values[];
}
