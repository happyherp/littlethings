from dataclasses import dataclass

@dataclass
class Person:
    age: int = 30
    gender: str = 'female'  # default gender set to female
    fatness: float = 1.0  # a float representing fatness (0.0) super skinny to (2.0) very fat.
    hair: str = 'black'  # description of hair
    ethnicity: str = 'european'  # expecting values like 'european', 'african', 'asian'
    attractiveness: float = 1.0  # a float representing attractiveness
    
    def createPrompt(self) -> str:
        prompt = f"/imagine a {self.age} year old {self.gender} with {self.hair} hair, "
        prompt += f"of {self.ethnicity} descent ::1 "
        if (self.fatness > 1.0):
            prompt += f"overweight ::{self.fatness-1.0} "
        elif (self.fatness < 1.0): 
            prompt += f"underweight ::{1.0-self.fatness} "
        return prompt




me = Person(age=35, gender="male", hair="brown")
print("me", me.createPrompt());

fatKid = Person(age=12, gender="male", fatness = 1.8)
print("fatKid", fatKid.createPrompt());

skinnyGrandma = Person(age=80, fatness=0.5)
print("skinnyGrandma:", skinnyGrandma.createPrompt())

