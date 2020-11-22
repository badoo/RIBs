# Rules for happy little trees

A central piece of RIBs' philosophy is to represent your application as a tree of individual pieces of functionality.

But how should you go about it?

## Example

It's probably easiest to understand the concept when demonstrated on things that we usually componentise anyway: screens.

![](https://i.imgur.com/HGI5gNg.png)

In this example, we have a bottom menu on this screen with three options left to right: ```My profile```, ```Card swiping game```, ```Messages```. Pressing any of them would show the corresponding screen in the content area. Right now the ```Card swiping game``` is active and seen on the screenshot.

Here, we could represent each of them as a separate ```Node``` in the tree:

![](https://i.imgur.com/NOaSvhf.png)

So why are they children to this ```Container```? Well, they don't just exist in a vacuum. All ```Nodes``` are organised on parent-child relationships, with the single exception being the ```Root``` itself, which doesn't have a parent.

The ```Container``` here is also a ```Node```. Its responsibility here is to decide which screen needs to be shown. 


## Going further

Let's imagine that when the ```Messages``` screen is shown, it first shows you a list of conversations:

![](https://i.imgur.com/NIuSGcI.png)

Then, clicking any of items in the list, we would see that conversation on full screen:

![](https://i.imgur.com/UIFI06u.png)


If we keep this logic on the "top level", it will quickly get very busy with lots of concerns. Rather, we can realise that the decision whether to show the list or the individual conversation never leaves the ```Messages``` screen!

As such, we can keep it its own implementation detail, and place these two screens as its children in the tree:

![](https://i.imgur.com/6qUQ4Nm.png)

Which means that we now have three levels in the tree actually:

![](https://i.imgur.com/dM7PO29.png)


## Delegate

While extracting screens as standalone ```Nodes``` rather explains itself, we should go further. 
 Going deeper, sections of the screen can be thought of as standalone components implementing a concern: extract them as children and delegate this responsibility to them.

[image]()

Also, it's not always about UI: you can extract and delegate pieces that add purely business logic without any UI too.


## Break down complexity

Even though a ```Node``` might start simple, it can get more and more busy over time. At any point if you think it's doing just too much, you can extract some logic (and / or UI), and move them into child ```Nodes```. 

This way, each of your individual ```Nodes``` can remain simple and easy to understand. 


## Single responsibility

Keep your ```RIBs``` with a single responsibility. If your ```RIB``` is doing more than one thing, consider breaking it apart.

This is the easiest to point out on the examle of ```Containers```. If your ```RIB``` have children and some logic to decide when to show which, then keep that its only responsibility. It's already enough! You can move all other things to those children as implementation details (from the perspective of the ```Container```).


## Stop thinking in screens

Coming from a background where we were using Android ```Activities```, it's understandable that we have a reflex to 1:1 model components to screens.

It's useful now though to stop doing that! Screens are simply components that just _happen to_ fill the entire available screen space. There's nothing special about them.

If we take the example with ```Containers``` further, we gain a super powerful tool though. A ```Container``` is just a level in the tree which keeps a single responsibility: choose which of its children to activate and delegate control flow to.

In this sense, when compared to old-school Android building blocks:

- an ```Application``` is a container of ```Activities```
- an ```Activity```is a container of ```Fragments```
- a ```Fragment``` is a container of nested ```Fragments```
- a ```ViewPager``` is a container of ```Fragments```
- a ```RecyclerView``` is a container of its items

So many different building blocks though to achieve something fundamentally similar: the idea of an abstract container, regardless of available screen space or even the presense of a UI. 

Taking this idea to its full potential, we can model "invisible" parts of our application as containers.

Let's imagine an app with authentication, profile creation, onboarding, and main screens that you'd see after you're done with all of those:

![](https://i.imgur.com/PWhCnc1.png)

- the ```Root``` of our application is a container of ```Logged out``` and ```Logged in``` 
- ```Logged out``` is a container of ```Splash screen```, ```Login screen``` and ```Create account flow```
- ```Logged in``` is a container of ```Onboarding``` and our main screens
- ```Create account flow``` and ```Onboarding``` are containers of their steps
- (not shown on diagram: all screens are comprised of further ```Nodes```)

In this example, all of these high level ```Containers``` represent an abstract concept only – not something that you can directly represent on the screen. 

Using this approach, you can have a single versatile tool to represent otherwise "invisible" parts of your application as standalone, lightweught components that delegate their functionality to their children.


## Visibility

One last piece of advice that you'll probably find useful: keep visibility in the tree to the bare minimum.

Rules are simple:

1. no ```Node``` should know anything about its parent
2. no ```Node``` should know anything about its sublings under the same parent
3. grandchild ```Nodes``` are considered implementation detail of children; every ```Node``` should know about its direct children only


This way:
- we can avoid different components to become coupled to each other
- we can easily change the tree if needed, as everything further below is 
- we can easily reuse any ```Node``` and plug it under any other parent
- we can easily reuse whole subtrees with the same amount of effort as a single leaf ```Node```




















