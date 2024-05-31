import {Component, OnInit} from '@angular/core';
import {Subscription} from "../../model/subscription";
import {Router} from "@angular/router";
import {SubscriptionService} from "../../service/subscription.service";
import * as bootstrap from "bootstrap";

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss']
})
export class SubscriptionComponent implements OnInit {
  protected subscriptions: Subscription[] = [];

  constructor(private router: Router,
              private subscriptionService: SubscriptionService) {
  }

  ngOnInit(): void {
    Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
      .forEach((tooltipTriggers: any) => new bootstrap.Tooltip(tooltipTriggers));
    this.subscriptionService.getSubscriptions().subscribe({
      next: (res: any) => this.subscriptions = res.data,
      error: (err: any) => console.log(err.error)
    });
  }

  updateSubscription(id: number) {
    this.router.navigate(['/users/update'], {queryParams: {id: id}}).then(() => {
    });
  }

  cancelSubscription(id: number) {

  }
}
