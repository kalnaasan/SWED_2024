import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import * as bootstrap from "bootstrap";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  protected users: User[] = [];

  constructor(private router: Router,
              private userService: UserService) {
  }

  ngOnInit(): void {
    Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
      .forEach((tooltipTriggers: any) => new bootstrap.Tooltip(tooltipTriggers));
    this.userService.getUsers().subscribe({
      next: (res: any) => this.users = res.data,
      error: (err: any) => console.log(err.error)
    });
  }

  updateUser(id: number) {
    this.router.navigate(['/users/update'], {queryParams: {id: id}}).then(() => {
    });
  }

  cancelUser(id: number) {

  }
}
