from production import AND, OR, NOT, PASS, FAIL, IF, THEN, \
     match, populate, simplify, variables
from zookeeper import ZOOKEEPER_RULES

# This function, which you need to write, takes in a hypothesis
# that can be determined using a set of rules, and outputs a goal
# tree of which statements it would need to test to prove that
# hypothesis. Refer to the problem set (section 2) for more
# detailed specifications and examples.

# Note that this function is supposed to be a general
# backchainer.  You should not hard-code anything that is
# specific to a particular rule set.  The backchainer will be
# tested on things other than ZOOKEEPER_RULES.


def backchain_to_goal_tree(rules, hypothesis):
    print "backchain_to_goal_tree", hypothesis
    possible = [hypothesis]

    for rule in rules:
        print "rule", rule
        for consequence in rule.consequent():
            bindings = match(consequence, hypothesis)
            if bindings != None:
                print "bindings", bindings
                sub_hypothesis = populate(rule.antecedent(), bindings)
                print "sub_hypothesis", sub_hypothesis
                sub_hypothesis_extended = extendGoalTree(rules, sub_hypothesis)
                possible.append(sub_hypothesis_extended)
    
    return simplify(OR(possible) )


def extendGoalTree(rules, goaltree):

    if (isinstance(goaltree, AND) or isinstance(goaltree, OR) or
        isinstance(goaltree, NOT)):

        return goaltree.__class__(*[extendGoalTree(rules, x)for x in goaltree])
    elif isinstance(goaltree, basestring):
        return backchain_to_goal_tree(rules, goaltree)
    else: raise ValueError, "Don't know how to extend a %s" % \
      type(goaltree)
                  

# Here's an example of running the backward chainer - uncomment
# it to see it work:
#print backchain_to_goal_tree(ZOOKEEPER_RULES, 'opus is a penguin')
