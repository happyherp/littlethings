from production import *
from backchain import *

def prove(rules,axioms,hypothesis):
    cond = backchain_to_goal_tree(rules, hypothesis)
    new_rule = IF(cond, THEN(hypothesis))
    return hypothesis in forward_chain([new_rule], axioms, verbose=False)


succession = IF('(?x) in N', THEN('S(?x) in N'))
addition_0 = IF('(?a) in N',THEN('(?a) p 0 = (?a)'))
addition_n = IF('(?a) p (?b) = (?c)',THEN('(?a) p S(?b) = S(?c)'))
mult_0 = IF('(?a) in N',THEN('(?a) t 0 = 0'))
mult_n = IF('(?a)0 t (?b) = (?c)', THEN('(?a)0 t S(?b) = (?a)(?c)' ))
even = IF('even (?a)', THEN('even SS(?a)'))
odd =  IF('even (?a)', THEN('odd S(?a)'))
noprime = IF('(?a) t (?b) = (?c)', THEN('noprime (?c)'))
prime = IF(NOT('noprime (?p)'), THEN('prime (?p)'))

rules = [succession, addition_0, addition_n, mult_0, mult_n, even,odd,noprime,prime]
axioms = ['0 in N', 'even 0', 'prime 1']


#goes on forever
#print forward_chain(rules, ['0 in N'], verbose=True)
#print forward_chain([addition_n],["1 + 2 = 3"], verbose=True)
#print forward_chain([addition_n],["1 p 2 = 3"], verbose=True)

#print backchain_to_goal_tree(rules, 'SSSS0 in N')
#print backchain_to_goal_tree(rules, 'SS0 p 0 = SS0')
#print backchain_to_goal_tree(rules, 'SS0 p SS0 = SSSS0')
print backchain_to_goal_tree(rules, '(?a) in N')

from unittest import TestCase,main

class Test(TestCase):

    def assertProvable(self, hyp):
        self.assertTrue(prove(rules,axioms, hyp))

    def assertUnprovable(self, hyp):
        self.assertFalse(prove(rules,axioms, hyp))

    def test_nat(self):
        self.assertProvable('0 in N')
        self.assertProvable('S0 in N')
        self.assertUnprovable('S in N')

    def test_add(self):
        self.assertProvable('SS0 p SSS0 = SSSSS0')
        self.assertUnprovable('SS0 p SSS0 = SSSS0')

    def test_mult(self):
        self.assertProvable('SS0 t 0 = 0')
        self.assertProvable('SS0 t SS0 = SSSS0')
        self.assertUnprovable('SS0 t SS0 = SSSSS0')

    def test_odd(self):
        self.assertUnprovable('even SSS0')      
        self.assertProvable('odd SSS0')
        self.assertProvable('even SSSS0')       
        self.assertUnprovable('odd SSSS0')

    def test_prime(self):
        self.assertProvable('prime 1')
        self.assertUnprovable('noprime 1')

        self.assertProvable('prime 2')
        self.assertUnprovable('noprime 2')        
        
        self.assertProvable('noprime 0')
        self.assertUnprovable('prime 0')
        
        self.assertProvable('noprime 4')
        self.assertUnprovable('prime 4')
        
        self.assertProvable('noprime 21')
        self.assertUnprovable('prime 21')

if __name__ == '__main__':
    main()
