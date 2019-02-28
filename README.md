# The Effect of Following Distance on an Environment Exploring Rover

## Intelligent Control & Cognitive Systems Coursework using LeJOS

This coursework required us to create a robot capable of following walls using a LeJOS EV3 rover. The code, written in Java, is available in the [src](https://github.com/Adamouization/ICCS/tree/master/src) folder.

## 1 Introduction

The goal of this research is to determine the effect of changing the wall following distance of a Lego EV3 rover on the number of times it collides with the edges of an arena. This experiment proves the hypothesis that a greater wall following distance results in fewer collisions. The wall following distance is related to both the time taken to traverse a given area and the number of collisions with the walls. It also directly affects the level of granularity with which the rover explores the more detailed parts of the enclosed area. Therefore an optimal traversal of the arena requires the most appropriate following distance to be found. The rover uses a combination of sensors and motors to navigate its environment. It is placed in an arena, consisting of fully enclosing walls.

The inspiration for the rover's initial design was the reactive subsumption architecture, with the system consisting of asynchronously communicating parallel layers, each responsible for sub tasks of the wall following algorithm (Brooks, 1991). However, subsequent experiment lead to an improved design being based on a simple reflex agent, an architecture which is implemented in series. The rover makes observations in real time and immediately acts on them. Wall following becomes an emergent property of the interactions between preset states of the environment and corresponding actions (Wooldridge, 2009). While implementing wall following, the rover was altered to store a "wall hit" state while finding the correct direction to turn. As a result, the final rover system is a model based reflex agent (Russell and Norvig, 1995). This design proved sufficient to traverse the arena and test the hypothesis.

## 2 Approach

Two types of sensors are used to allow the rover to collect data describing the environment. A pair of touch sensors are mounted on the front of the rover. A bar connects the two sensors for rigidity, allowing angular collisions with corners to be detected. An ultrasonic sensor is mounted on a rotating servo motor at the front of the rover. The high noise floor of the sensor is compensated for by averaging the readings and removing any which are outside of a range of the standard deviation from the mean. The motors were adjusted to compensate for tyre friction by turning more than the desired angle, allowing the rover a more accurate rotation. The rover has "wings" attached on each side of the main body. This was a morphological design decision, preventing the rover from being flipped over by collisions with low objects. The wings can also help to drag the rover around corners.

The rover software has three states which can be transitioned between: initial, searching and wall following.

![flowchart](https://github.com/Adamouization/ICCS/blob/master/report/figures/flowchart/System-Flowchart.png)

The rover begins in the initial state, travelling forwards until a collision is detected. The rover then enters the searching state. The possible configurations of the position of the rover in relation to the arena walls have been simplified to situations observable by the ultrasonic sensor sweeping left and right. The rover then turns by 90 degrees and rotates the ultrasonic sensor to face the wall.  To ensure that the rover makes a full clockwise circuit of the arena, it always makes a right turn if possible. Finally, the rover enters the wall following state and begins moving parallel to the wall. The ultrasonic sensor is used to maintain a constant distance. The rover continues in this state until the touch sensors are triggered again.

An arena has been created with enclosing walls, using a variety of concave and convex corners to provide a challenging path for the rover to traverse (Appendix \ref{sec:arena-layout}). The shape of the arena is preserved between trials. A single trial consists of one full lap of the arena. The distance maintained from the wall is an independent variable. The time taken to traverse back to the starting point and the number of wall collisions are dependent variables which are recorded. Two trials are performed for each distance. If the rover becomes stuck or pushes a wall the trial is aborted. The battery charge is kept consistent.

![arena layout](https://github.com/Adamouization/ICCS/blob/master/report/figures/arena-layout/Arena-Layout.png)

## 3 Results

One main hypothesis was tested by this experiment: maintaining a greater distance during the wall following state results in fewer collisions during a circuit.
The data collected from the experiment is consistent with this hypothesis. Increasing distance from the wall results in a downward trend in collisions and shorter lap times, as depicted in the graph below. A strong correlation between number of collisions and the length of time taken to complete a lap can be seen. It was also observed from visual comparisons of video footage that for larger distances, the rover completely avoided the first book pile, verifying that such distances result in lower detail traversals of the arena, which can be seen [here](https://youtu.be/VHsgZ4Ex2-c?t=159).

![results](https://github.com/Adamouization/ICCS/blob/master/report/figures/results_graph.png)

## 4 Discussion

This experiment demonstrated that maintaining a greater distance from the arena walls resulted in fewer collisions. This is particularly important as the searching state entered on collision contributes the longest time spent immobile. The number of collisions is clearly linked to the length of time taken to complete a circuit.

Shorter following distances made the rover susceptible to repeated collisions with flat surfaces due to the fixed turning amount while in the wall following state, shown [here](https://youtu.be/VHsgZ4Ex2-c?t=243). This resulted in many more collisions than necessary, and greatly increased the traversal time. To rectify this, the turning angle could be made proportional to the distance from the wall.

## 5 Conclusion

It is shown that a rover which follows walls at a greater distance completes a circuit of an arena with less collisions. It was also observed that a larger distance resulted in a less detailed exploration of the arena. The wall following distance is therefore an important variable which governs the trade-off between speed and accuracy of a traversal.

## YouTube Video

The full YouTube video can be found here: [The Effect of Following Distance on an Environment Exploring Rover - CM30229 ICCS - LeJOS](https://www.youtube.com/watch?v=VHsgZ4Ex2-c)

## References

(Brooks, 1991) Brooks, R. A. (1991). Intelligence without representation. Artificial intelligence, 47(1-3):139–159.

(Russell and Norvig, 1995) Russell, S. and Norvig, P. (1995). Artificial intelligence: A modern approach. Prentice Hall, volume 2.

(Wooldridge, 2009) Wooldridge, M. (2009).An introduction to Multiagent Systems. John Wiley & Sons. Chapter 5.1, pages 85-92.
