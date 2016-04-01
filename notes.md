# Notes #

I'm not too sure about how much you're expecting in this exercise, so I thought I'd scribble down some notes about what
I did and what my thoughts were in case it helps.

## Running the application ##

  * Install Gradle 2.12 and Sun Java 1.8
  * Run 'gradle eclipse' to generate the Eclipse classpath
  * Run 'gradle run' to build and start the server
  
Tests can be run in Eclipse (AccountResourceTest). I haven't hooked up the Gradle runner to the test task for the sake
of keeping things simple, but that's an obvious thing that should be done immediately if this were a proper project.

## General development process ##

The requirement to be a standalone server wasn't something I've done in Java before, and I picked Jetty and Jersey just
based recommendations online. I kept Jackson and Guice because I've used both before and thought they might be useful
(though in the end they turned out not to be really needed). Due to unfamiliarity with Jersey/Jetty and some issues with
the latest version of Jersey with Guice, getting a hello-world working with the basic stack took me longer than I liked,
but it was educational and useful.

From there I worked mostly TDD, putting in account creation, then deposits and withdrawals, then transfers. There's a
lot more to do (preventing accounts going negative, recording transactions, making the data store more realistic, etc.)
so I didn't really know where to stop. The exercise seemed to be asking for a simple REST API, though, so I stopped as
soon as it started getting much more complex. I hope I didn't quit too early.

## REST API ##

I've got a feeling that there are better ways to model this, but here's what I came up with. I'm sorry that I'm just
documenting this in text -- there are better tools (we've used Swagger previously) but to keep it simple I left it out.

### POST /accounts ###

Creates a new account with zero balance and returns it, returns Status.CREATED if successful.

```
{"id":"4c3b12a3-4d92-41fa-88e0-51dce4c175fa","balance":0.00}
```

"id" is a GUID (which is annoying if you're testing this with CURL -- apologies), balance is the balance of the account
as a BigDecimal internally. 

I think there's a problem here with the representation of balance on the wire -- it looks like Jackson is serialising
the BigDecimal as a double in JSON, so there's obviously a risk of overflow and rounding. This could be fixed by
registering a custom serialiser in Jackson for the type and writing it out as a String, and we'd have to require that
clients parse the string format. I didn't do that for the sake of keeping things simple.
    
### GET /accounts/:id ###

Gets the account with the specified ID and returns Status.OK, or returns Status.NOT_FOUND if the account cannot be
accessed. The account representation is the same as above.

### POST /accounts/:id/deposit ###

Creates a deposit for the specified account, returning the updated account and Status.CREATED if successful, or
Status.NOT_FOUND if the account cannot be accessed.

```
{"amount":0.00}
```

I have a feeling that there are better ways to model deposit/withdraw/transfer. These are verbs rather than nouns so
representing them as resources feels a little wrong, but it felt the most appropriate out of the alternatives I could
think of. I thought of deposit/withdrawal/transfer as different types of transaction that could be created, so I'm
returning Status.CREATED in this case.

### POST /accounts/:id/withdrawal ###

Creates a withdrawal for the specified account, returning the updated account and Status.CREATED if successful, or
Status.NOT_FOUND if the account cannot be accessed.

Just the inverse of deposit, with the JSON representation. I thought of having a single class (say, Amount) rather than
having Deposit and Withdrawal classes that are basically the same, but I thought these classes would diverge if more
detail were added to the example, so I kept them separate. 

### POST /accounts/:id/transfer ###

Creates a transfer from specified account to the target account, returning the updated account and Status.CREATED if
successful, or Status.NOT_FOUND if either account cannot be accessed.

```
{"targetAccountId":"4c3b12a3-4d92-41fa-88e0-51dce4c175fa","amount":0.00}
```

## Testing ##

On my current project we've tried switching away from a very strict mockist approach to something a little more
pragmatic based around integration tests, so I wanted to try something similar here. I TDD'd the APIs, but with a simple
example there's not much to test. The tests are given/when/then (or arrange/act/assert) style, again because it's what
I'm currently used to.

I haven't tested concurrency -- I thought that would be a bit artificial with a fake data store. I haven't tested data
limits, either, just because I wanted to keep it fairly simple and was a little short of time.

The tests aren't completely independent, unfortunately. The withdrawal test depends on correct functioning of deposit,
for example, so if someone broke deposit then withdrawal tests may also fail, possibly causing confusion. It's not
ideal, but it doesn't seem too bad right now so I left it alone.

## Hacky limitations ##

There were a few things that occurred to me while hacking this up that I haven't done, so I thought I'd mention them
just in case:

  * Haven't dealt with owners of accounts
  * Haven't dealt with closing accounts (maybe DELETE on /accounts/:id)
  * Haven't really dealt with concurrency because the data storage is just a placeholder
  * Haven't really got a good abstraction for transactions against the data store for the same reason
  * All the logic for updating accounts is in the REST methods for simplicity; that won't work when the logic gets more
    complex, so it should really be pulled out of there
  * Haven't versioned the transport
  * Haven't got an internal data model, just storing transport for simplicity
  * If Deposit/Withdrawal/Transfer are actual nouns then maybe they should be retrievable (GET
    /accounts/:id/deposit/:num), should have an ID and a time stamp
  * Haven't recorded transactions, which the user is probably interested in retrieving. Would need to think about paging
    in the REST API for that
  * Haven't made classes final ("Design and document for inheritance or else prohibit it"). In the past I would do this
    religiously because I was working on SDKs or extensible frameworks, but as this is just an example I didn't spend
    time on it

# Final thoughts #

Just wanted to say thanks for your time, and I enjoyed the exercise.
