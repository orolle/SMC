/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turnstile;

import rx.Observable;

/**
 *
 * @author Oliver Rolle <oliver.rolle@the-urban-institute.de>
 */
public class TurnstileRx {
  Observable<Boolean> isEnoughValue(Double value) {
    return Observable.just(value >= 1);
  }

  Observable<Void> unlock() {
    System.out.println("Turnstile.unlock()");
    return Observable.just(null);
  }

  Observable<Void> alarm() {
    System.out.println("Turnstile.alarm()");
    return Observable.just(null);
  }

  Observable<Void> thankyou() {
    System.out.println("Turnstile.thankyou()");
    return Observable.just(null);
  }

  Observable<Void> lock() {
    System.out.println("Turnstile.lock()");
    return Observable.just(null);
  }
}
