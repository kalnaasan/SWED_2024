import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly url = 'http://localhost:8080/api/v1/users'

  constructor(private http: HttpClient) {
  }

  public getUsers(): Observable<any> {
    return this.http.get<any>(this.url)
  }

  public getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.url}/${id}`);
  }

  public createUser(user: User): Observable<any> {
    return this.http.post<any>(`${this.url}`, user);
  }

  public deleteUser(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
