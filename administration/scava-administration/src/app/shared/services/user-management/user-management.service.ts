import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUser, User } from '../../../layout/user-management/user-model';
import { LocalStorageService } from '../authentication/local-storage.service';

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

  get() {
    if (this.jwtToken == null) {
      this.jwtToken = this.localStorageService.loadToken();
    }
    return this.httpClient.get(`${this.resourceUrl}/${this.users}`, { headers: new HttpHeaders({ 'Authorization': this.jwtToken }) });
  }
}