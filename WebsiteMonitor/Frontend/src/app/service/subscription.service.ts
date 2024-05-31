import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Subscription} from "../model/subscription";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  private readonly url = 'http://localhost:8080/api/v1/subscriptions'

  constructor(private http: HttpClient) {
  }

  public getSubscriptions(): Observable<any> {
    return this.http.get<any>(this.url)
  }

  public getSubscriptionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  public createSubscription(subscription: Subscription): Observable<any> {
    return this.http.post<any>(`${this.url}`, subscription);
  }

  public deleteSubscription(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
