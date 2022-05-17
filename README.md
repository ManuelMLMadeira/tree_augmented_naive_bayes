# Tree Augmented Naive Bayes Classifier

This repository contains the implementation of a classifier using the Tree Augmented Naive Bayes method. It is implemented in *Java*. Our initial motivation was to classify biomedical data, but its domain of application goes beyond it (*e.g.* stock market forecast, optical character recognition, etc.).

---
## Data:

The data is expected to be in the supervised learning "standard" format: a matrix $M \times N+1$, where each column corresponds to a different feature and each row to a different sample. The last column is considered to be the target, *i.e.*, the class that we want to predict, given a sample with all the features filled. Thus, there are $M$ samples in the dataset, each with $N$ features and the respective class.

The implementation provided in this repository was developed to deal with data in the same format as the one provided in the [*UCI machine learning repository*](http://archive.ics.uci.edu/ml/).

---
### Method: **Tree Augmented Naive Bayes Classifier**

We try to infer the distribution that generated data via Bayesian inference. However, instead of simply applying the Naive Bayes method, the Tree Augmented Naive Bayes (TAN) method relaxes the feature independence assumption, imposing that each feature only depends on the class and one other feature. This assumption imposes a tree structure in the Bayesian Network (that represents the dependencies across features).

But why this assumption?
+ Dependence of all the features on the class:
  + In principle, a feature can not be independent of the class, since otherwise, that feature would not serve to classify (find the class of) that sample;
+ Dependence of each feature on one (and only one) other:
  + Finding the graph of a Bayesian Network that maximizes the likelihood of the data is known to be an NP-complete problem;
  + Even in the cases where we find the graphs that maximize the likelihood, those tend to be complete graphs (instead of sparse), being highly prone to overfitting of the data;
  + Thus, a solution consists of constraining the graph learning task to simpler structures;
  + The only case known in which there is an efficient algorithm that maximizes the likelihood of the data is the one in which the features form a tree;
  + To find the TAN that maximizes the likelihood, we resort to an algorithm that finds the maximum weighted spanning tree: we use the [Prim's algorithm](https://en.wikipedia.org/wiki/Prim%27s_algorithm) (but there are other alternatives).


---
## Implementation

We provide two applications:

### ***Apprendiz***

Generates the TAN (Bayesian Network) that maximizes the likelihood of the data provided
This data must be provided as a *.csv* file. 
The end product of this application will be saved as a *.ser* file.

### ***DrHouse***

Classifies a sample provided by the user. This sample must consist of integers separated by commas. This classification is made considering the Bayesian Network that is input to this application.


