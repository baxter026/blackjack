# blackjack

A (lame and unfinished) attempt at implementing the [NEAT algorithm](http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf) to learn to play soft 17 blackjack.

The goal was to have the AI learn to play blackjack [in the most optimal way](https://www.blackjack-chart.com/dealer-stands-on-soft-17/) with as little influence valuing hands from me

#### The neural network has 3 inputs and 2 outputs

- Player hand value(4 - 20)
- Player soft ace present(0 or 1)
- Dealer value(2-11)
- Double decision(double or continue to next option)
- Hit decision(Hit or stand)


#### Unfinished elements

- Bet size output and wallet input has not been added. Also posibly a streak input to go along with this to see if bets would be increase/decreased during a winning/losing streak
- Split decision has not been added although the players hand is already kept in an array and the hand in played in a for loop that only runs once. Would be easy to add  
- Threading

#### Issues
The outputs seem to follow a "linear" trajectory. The NN tends to either disregard to dealer value or not value it strong enough. 
  
##### Examples:
  
- If the user has a 16 it will either hit or stand on all dealer cards. There are some cases where it develops to have different outcomes on a user card but it    follows a 'diagonal' pattern in the chart that is printed. A playerr with 12 will stand until the dealer has 5. When the player has 13 it will stand until the dealer has 6. When a dealer has 14 it will stand until the dealer has 7 and so on. 
   
- Weaknesses in the design are more apparent when doubling. When playing blackjack a user without an ace should only double when they have 9. 10 or 11 in their starting hand. The 'linear' result can never double a 10 and not double an 8.

- The only potential fix to this I can think of would be to add more inputs to better represent the 'non-linear' odds of blackjack. I've been avoiding that to not just make the neural network remember every decision individually but the current input/output doesn't work.

Statistics class is no longer used in the initial way it was intended to.
- The class was meant to keep the best players stats and the overall stats
- Currently only keeps overall stats but best player stats are still implemented
- Could be changed to save every individuals stats
