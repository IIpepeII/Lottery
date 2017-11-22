import {Injectable} from '@angular/core';
import {ResultNum} from './resultNum';
import {Token} from './token';

import {HttpClient, HttpHeaders} from '@angular/common/http';

const token = new Token();
const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': token.token
    })
};

@Injectable()
export class FourMatchFiveRatioService {
    private result;
    private resultByDate;
    private lotteryUrl = 'http://localhost:8080/lottery//lottery/four-match-ratio-to-five-match';

    constructor(private http: HttpClient) {
    }

    getResult(): ResultNum {
        this.result = new ResultNum();
        this.http
            .get<number>(this.lotteryUrl, httpOptions).subscribe(data => this.result.num = data['result']);
        return this.result;
    }

    getResultByDate(from: string, to: string): ResultNum {
        const url = this.lotteryUrl + '/' + from + '/' + to;
        this.resultByDate = new ResultNum();
        this.http
            .get<number>(url, httpOptions).subscribe(data => this.resultByDate.num = data['result']);
        return this.resultByDate;
    }
}