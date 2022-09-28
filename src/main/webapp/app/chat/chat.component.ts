import { Component, OnInit } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../core/config/application-config.service';

@Component({
  selector: 'jhi-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit {
  title = 'WebSocketChatRoom';

  greetings: string[] = [];
  disabled = true;
  newmessage?: string;
  private stompClient = null;
  protected resourceUrl = this.applicationConfigService.getEndpointFor('testchat');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  ngOnInit() {
    this.connect();
  }

  setConnected(connected: boolean) {
    this.disabled = !connected;

    if (connected) {
      this.greetings = [];
    }
  }

  connect() {
    const socket = new SockJS(this.resourceUrl);
    this.stompClient === Stomp.over(socket);

    const _this = this;
    // @ts-ignore
    this.stompClient?.connect({}, function (frame: string) {
      console.log('Connected: ' + frame);

      // @ts-ignore
      _this.stompClient?.subscribe('/start/initial', function (hello: { body: string }) {
        console.log(JSON.parse(hello.body));

        _this.showMessage(JSON.parse(hello.body));
      });
    });
  }

  sendMessage() {
    // @ts-ignore
    this.stompClient?.send('/current/resume', {}, JSON.stringify(this.newmessage));
    this.newmessage = '';
  }

  showMessage(message: string) {
    this.greetings.push(message);
  }
}
