

IT 328, Introduction to the Theory of Computation

Programming Assignment 2 (Team work)

N FA → DFA → Minimized DFA

Due date: Nov. 17, 2022, Thursday, 11:55 PM

100 points (90 on programs, 10 on report)

As the ﬁrst programming assignment, form a team of 3 students and decide who will be the corresponding

student for this programming assignment. If you are content with your previous team, you don’t have to

change your team, but A/B/C roles and the corresponding student don’t have to be the same. However,

you still have to use the ReggieNet to join the team for my records (it is a new join-able set for this

assignment). The major programming responsibility is divided as follows.

• Student A should write a method that takes an NFA from a text ﬁle and convert it to an equivalent

DFA. The DFA should be shown on the screen and written to a text ﬁle for Student B and Student

C to use.

• Student B should write a method that takes a DFA from a text ﬁle created by Student A and

minimize it to an optimal DFA. The optimal DFA should be shown on the screen and written to a

text ﬁle for Student C to use.

• Student C should write a method that takes a DFA from students A and B and use it to parse

strings attached to the original NFA ﬁle (alternatively, strings in a DFA ﬁle if conversion fails).

\- The task for Student C seems more straightforward. The team may, but doesn’t have to, ask Student

C to be in charge of facilitating supporting tools, such as to design a Set class that can perform basic

set operations like union, intersection, membership and equality tests, and add/remove elements.

N FA ﬁle format: (Q, Σ, δ, q0, A) Consider ﬁle X.nfa as an example, we have

|Q|: 5

Sigma: a b c d

\----------------------------------------

0: {1} {2} {} {} {0,1}

1: {1} {1,2,4} {1} {1} {1}

2: {2,4} {0} {1,3} {2} {2}

3: {0} {1,3} {3} {2,4} {3}

4: {3} {} {1} {3,4} {0,2,4}

\----------------------------------------

Initial State: 3

Accepting Sate(s): 3,4

-- Input strings for testing -----------

aabacadbdcacbacadbacaabacdcbbcadbcadbacabacabacdacabca

....

• The ﬁrst and second lines indicate the number of states and Σ. In this case, X.nfa has 5 states,

indexed from 0 to 4, and Σ = {a, b, c, d}.





IT 328

Programming Assignment 2 (Team work)

S

• The transition function, δ : Q × Σ {λ} → 2Q, is presented in a transition table between two dash

lines, where the number followed by a colon is the index of the state, then each set is the set of next

states on each input alphabet corresponding to the alphabets in the same order shown in the Sigma

line. The extra set at the end of each line is the λ-transition of the state. For example, consider

q of X.nfa above. We have: δ(q , a) = {q }, δ(q , b) = {}, δ(q , c) = {q }, δ(q , d) = {q , q }, and

4

4

3

4

4

1

4

3

4

δ(q , λ) = {q , q , q }. Note that, the last set is referred to the λ-transition, and it always contains

4

0

2

4

the state itself, i.e., q is included in δ(q , λ); likewise q ∈ δ(q , λ) and q ∈ δ(q , λ), and so on.

0

0

1

1

2

2

• After the transition table, the initial sates and accepting states are speciﬁed . In this example, q is

3

the initial state and the set of accepting states A is {q , q }.

3

4

• The rest of the ﬁle contains strings as the input strings for testing (after convert to a DFA and

minimization. Note that: There are 30 strings and the ﬁrst string is always the empty string.

D FA ﬁle format: (Q, Σ, δ, q0, A) Consider X.dfa s an example, we have

|Q|:

6

a

Sigma:

b

c

d

\------------------------------

0:

1:

2:

3:

4:

5:

1

4

1

5

4

5

2

3

5

3

3

5

0

4

2

2

4

2

3

4

3

5

4

5

\------------------------------

Initial State: 0

Accepting Sate(s): 0,2,3,5

-- Input strings for testing -----------

aabacadbdcacbacadbacaabacdcbbcadbcadbacabacabacdacabca

....

• The format and presentation are similar to DFA ﬁle except that the type of the transition function

becomes δ : Q × Σ → Q, that allow us to make a better alignment for the transition table. Also,

there is no λ-transition in DFA.

What to do

\1. Make a new directory ∼/IT328/nfadfa on your unix account, where ∼ is your home directory. All

ﬁles related to this assignment should be prepared in this directory.

\2. From /home/ad.ilstu.edu/cli2/Public/IT328/nfadfa, copy every ﬁles into to your own nfadfa

directory. There are some NFA and DFA ﬁles, where the ﬁle name’s extension indicates it is an

NFA or a DFA. Note that X.dfa is an equivalent DFA version of X.nfa. Every given NFA/DFA ﬁle

contains 30 test strings. A.nfa’s DFA and minimized versions are given for you to compare.

Due date: Nov. 17, 2022, Thursday, 11:55 PM

ꢀc Chung-Chih Li P-2/[4](#br4)





IT 328

Programming Assignment 2 (Team work)

\3. Name your main program as NFA2DFA.java that will take one argument from the command line. I

will compile and run your program from the Unix command line as follows:

javac NFA2DFA.java

java NFA2DFA X.nfa

The argument is an NFA ﬁle as the aforementioned X.nfa. Your program should check every string

attached to the NFA ﬁle (including an empty string) and print Yes or No on the screen for each

string to indicate whether the NFA accepts the string. Your program will not parse the input string

using the NFA directly, which is rather ineﬃcient since we have to rely on back-tracking. Instead,

the NFA should be converted to an equivalent DFA, and use it to parse every string attached to the

NFA ﬁle before and after minimization.

The output of your program should follow the exact format shown in the sample output in the next

page, including the following results on the screen:

(1) An equivalent DFA (before minimization) as shown in the sample output.

(2) A list of answers of the DFA on each of the 30 strings attached at the end of the NFA ﬁle.

Arrange your program to show exactly 15 answers in a line on the screen.

(3) Minimize the DFA and show the optimal DFA using the same format on the screen.

(4) A list of answers of the optimal DFA on the same 30 strings. If your program is correct, the DFA

and its minimized version should give the same results.

Incomplete Works: In case some of the jobs fail, you may get partial credit up to 70% depending

on how much the team has accomplished. You have to skip the failing part and don’t let your program

terminate with an error exception. In general, there are 3 main tasks in this assignment. If one task

fails, you may get up to 70% of the credit; if two fail, you may get up to 50% of the credit. In the

following two cases, you have to make some extra adjustment for me to know.

(1) (Up to 70% ) If Student B can successfully minimize a DFA to an optimal one but Student A

fails to convert an NFA to a DFA for B, then don’t submit NFA2DFA.java. In stead, submit a

program named minimizeDFA.java that will read in a DFA ﬁle and minimize it.

javac minimizeDFA.java

java minimizeDFA X.dfa

The output should be the same except that the ﬁrst line massage should be “Minimize X.dfa”

and the testing strings should be the 30 string attached at the end of the DFA ﬁle.

(2) (Up to 50% ) If both Student A and Student B fail, the team should submit a program named

parse.java that will read in a DFA ﬁle and run it on the attached 30 strings.

javac parse.java

java parse X.dfa

The output should be the same up to the minimization section except that the ﬁrst line massage

should be “Run X.dfa” and the testing strings should be those attached at the end of X.dfa.

Due date: Nov. 17, 2022, Thursday, 11:55 PM

ꢀc Chung-Chih Li P-3/[4](#br4)





IT 328

Programming Assignment 2 (Team work)

\4. Final Step on Unix Account & Report are same as the previous assignment. Submit your

report through corresponding student’s ReggieNet and use bash script submit328.sh to prepare

your program on your Unix account. (You don’t have to print the program outputs on your report,

they are lengthy; I will run your program and check.)

Sample output:

java NFA2DFA X.nfa strings.txt

NFA X.nfa to DFA X.dfa:

Sigma:

a

b

c

d

\------------------------------

0:

1:

2:

3:

4:

5:

1

4

1

5

4

5

2

3

5

3

3

5

0

4

2

2

4

2

3

4

3

5

4

5

\------------------------------

0: Initial State

0,2,3,5: Accepting State(s)

Parsing results of strings attached in X.nfa:

Yes No

No

Yes No Yes Yes No

No Yes No

Yes No Yes Yes

No Yes Yes Yes No No

Yes Yes No No

Yes Yes No No

Yes

Yes:16 No:14

Minimized DFA from X.dfa:

Sigma:

a

b

c

d

\------------------------------

0:

1:

2:

3:

1

1

1

3

2

3

3

3

0

1

2

2

3

1

3

3

\------------------------------

0: Initial State

0,2,3: Accepting State(s)

Parsing results of strings attached in X.nfa:

Yes No No Yes No Yes Yes No No Yes No

No Yes Yes Yes No No Yes Yes No No

Yes No Yes Yes

Yes Yes No No

Yes

Yes:16 No:14

|Q| 6 -> 4

The last line indicates the number of states reduced from 6 to 4.

Due date: Nov. 17, 2022, Thursday, 11:55 PM

ꢀc Chung-Chih Li P-4/[4](#br4)

