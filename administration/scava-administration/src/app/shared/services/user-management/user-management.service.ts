import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, IUser } from '../../../layout/user-management/user-model';
import { LocalStorageService } from '../authentication/local-storage.service';
import { debug } from 'util';

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {
  
  private resourceUrl: string = environment.SERVER_API_URL + '/api';
  private users: string = 'users';
  private jwtToken: string = null;

  constructor(
    private httpClient: HttpClient,
    private localStorageService: LocalStorageService
  ) { }

  update(user: User): Observable<IUser> {
    if(this.jwtToken == null) {
      this.jwtToken = this.localStorageService.loadToken();
    }
    return this.httpClient.put(`${this.resourceUrl}/${this.users}`, user, { 
      headers: new HttpHeaders({ 
        'Authorization': this.jwtToken
      }) 
    });
  }

  find(login: string) {
    if (this.jwtToken == null) {
      this.jwtToken = this.localStorageService.loadToken();
    }
    return this.httpClient.get(`${this.resourceUrl}/${this.users}/${login}`, {
      headers: new HttpHeaders({
        'Authorization': this.jwtToken
      })
    });
  }

  query() {
    if (this.jwtToken == null) {
      this.jwtToken = this.localStorageService.loadToken();
    }
    return this.httpClient.get(`${this.resourceUrl}/${this.users}`, { 
      headers: new HttpHeaders({ 
        'Authorization': this.jwtToken 
      }) 
    });
  }

  delete(login: string) {
    if(this.jwtToken == null) {
      this.jwtToken = this.localStorageService.loadToken();
    }
    return this.httpClient.delete(`${this.resourceUrl}/${this.users}/${login}`, { 
      headers: new HttpHeaders({ 
        'Authorization': this.jwtToken 
      })
    });
  }
}
