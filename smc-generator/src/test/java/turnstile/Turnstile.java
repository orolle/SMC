/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turnstile;

import rx.Observable;
import turnstile.TurnstileContext.unlock;

/**
 *
 * @author Oliver Rolle <oliver.rolle@the-urban-institute.de>
 */
public class Turnstile {

  boolean isEnoughValue(Double value) {
    return value >= 1.0;
  }
  
  public static void main(String[] args) {
    TurnstileContext sm = new TurnstileContext(null);
    
    sm.isEnoughValue = v -> Observable.just(v >= 1.0);
    sm.unlock = new unlock() {
      @Override
      public <T> Observable<T> apply() {
        return ((Observable<T>) Observable.just(1));
      }
    };
    sm.deactivateAlarm = new TurnstileContext.deactivateAlarm() {
      @Override
      public <T> Observable<T> apply() {
        return ((Observable<T>) Observable.just("Hello World"));
      }
    };
    
    sm.<String>coin(2.0).
      subscribe(d -> { System.out.println("onNext(): "+d); }, e -> e.printStackTrace(), () -> {System.out.println("completed()");});
  }
}
