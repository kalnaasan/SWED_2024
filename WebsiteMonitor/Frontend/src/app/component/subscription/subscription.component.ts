import {Component, OnInit} from '@angular/core';
import {Subscription} from "../../model/subscription";
import {Router} from "@angular/router";
import {SubscriptionService} from "../../service/subscription.service";
import * as bootstrap from "bootstrap";
import {Frequency} from "../../model/frequency";
import {CommunicationChannel} from "../../model/communication-channel";

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss']
})
export class SubscriptionComponent implements OnInit {
  protected subscription: Subscription = {
    id: -1,
    websiteName: '',
    url: '',
    frequency: Frequency.NONE,
    communicationChannel: CommunicationChannel.SMS,
    lastUpdate: new Date()
  };
  protected subscriptions: Subscription[] = [];
  protected isUpdate: boolean = false;

  protected readonly Frequency = Frequency;
  protected readonly CommunicationChannel = CommunicationChannel;

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

  createSubscription() {
    if (!this.isUpdate) {
      this.subscriptionService.createSubscription(this.subscription).subscribe({
        next: (res: any) => this.subscriptions.push(res.data),
        error: (err: any) => console.log(err.error)
      });
    }
  }

  updateSubscription() {
    if (this.isUpdate) {
      this.subscriptionService.createSubscription(this.subscription).subscribe({
        next: (res: any) => console.log(res.data),
        error: (err: any) => console.log(err.error)
      });
    }
  }

  cancelSubscription(id: number) {

  }
}
