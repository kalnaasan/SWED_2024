import {CommunicationChannel} from "./communication-channel";
import {Frequency} from "./frequency";

export interface Subscription {
  id: number
  websiteName: string
  url: string
  frequency: Frequency
  communicationChannel: CommunicationChannel
  lastUpdate: any
}
