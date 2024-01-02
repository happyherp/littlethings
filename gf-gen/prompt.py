from dataclasses import dataclass

NEUTRAL_POSE = "https://cdn.discordapp.com/attachments/1189435958400131072/1189921466989432903/carlosamigo__a_mannequinn_standing_straight_arms_folded_all_whi_35a7af27-92a0-4e1e-8ded-499e1970f841.png?ex=659febb9&is=658d76b9&hm=fe18bcbaada51cbf26f0f9e541d72885519a57124a9801330e341958d6fe986e&"

@dataclass
class Person:
    age: int = 30
    gender: str = 'female'  # default gender set to female
    fatness: float = 1.0  # a float representing fatness (0.0) super skinny to (2.0) very fat.
    hair: str = 'black'  # description of hair
    ethnicity: str = 'european'  # expecting values like 'european', 'african', 'asian'
    attractiveness: float = 1.0  # a float representing attractiveness
    features: tuple[str] = () # extra attributes
    eyes: str = "brown" 
    
    def createPrompt(self) -> str:
        nos = ["airbrush", "filter", "make up"]
        
        #basic
        prompt = (f"/imagine \n{NEUTRAL_POSE} a {self.age} year old {self.gender} with {self.hair} hair, "
                  f"of {self.ethnicity} descent, "
                  f"with {self.eyes} eyes, "
                  "wearing jeans and a white T-Shirt and sneakers, "
                  "full body, making eye contact, hands, "
                  "photoshoot, Studio Lighting, ")
        prompt += ", ".join(self.features)
        prompt += " ::1 "
        #prompt += "shoes :: 2 face :: 2 " #force full body
        
        #weighted
        if (self.fatness > 1.0):
            prompt += f"overweight ::{self.fatness-1.0} "
        elif (self.fatness < 1.0): 
            prompt += f"underweight ::{1.0-self.fatness} "
        if (self.attractiveness > 1.0):
            prompt += f"attractive ::{self.attractiveness-1.0} "
        elif (self.attractiveness < 1.0): 
            prompt += f"ugly ::{1.0-self.attractiveness} "  
            nos.append("beautiful")

        #parameters
        prompt += "--style raw "
        prompt += "--ar 1:2 "
        if (len(nos) > 0): 
            prompt += "--no " + ", ".join(nos)
        prompt += "--seed 654654 "
        prompt += "--stylize 0 "
        return prompt


def permutate():
    
    i = 1
    for age in (18, 25, 32):
        for fat in (0.3, 1.0, 1.3, 2.0):
            person = Person(age=age, fatness=fat)
            print(i, person.createPrompt())
            i+=1


def makeSomePeople():

    me = Person(age=35, gender="male", hair="brown")
    print("me", me.createPrompt());

    fatKid = Person(age=12, gender="male", fatness = 1.8)
    print("fatKid", fatKid.createPrompt());

    skinnyGrandma = Person(age=80, fatness=0.5)
    print("skinnyGrandma:", skinnyGrandma.createPrompt())

    ugly18 = Person(age=18, attractiveness=0.0)
    print("ugly18:", ugly18.createPrompt())

    ginger_nerd = Person(age=25, hair="long ginger", features = ("freckles", "glasses"), eyes="green")
    print("ginger_nerd:", ginger_nerd.createPrompt())
    
    
    
permutate()