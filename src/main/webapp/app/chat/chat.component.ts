import { Component, OnInit } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

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
  private stompClient?: any;

  ngOnInit(): void {
    this.connect();
  }

  setConnected(connected: boolean): void {
    this.disabled = !connected;

    if (connected) {
      this.greetings = [];
    }
  }

  connect(): void {
    const socket = new SockJS('http://localhost:9000/testchat');

    this.stompClient = Stomp.over(socket);

    const _this = this;

    this.stompClient.connect({}, function (frame: string) {
      console.log('Connected: ' + frame);

      _this.stompClient.subscribe('/start/initial', function (hello: { body: string }) {
        console.log(JSON.parse(hello.body));

        _this.showMessage(JSON.parse(hello.body));
      });
    });
  }

  sendMessage() {
    this.stompClient?.send('/current/resume', {}, JSON.stringify(this.newmessage));
    this.newmessage = '';
  }

  showMessage(message: string) {
    this.greetings.push(message);
  }
}
