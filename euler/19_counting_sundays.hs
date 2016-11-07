

type Weekday = Int -- 0=Monday 6=Sunday
type Year = Int
type Month = Int --1 to 12
  
leapYear :: Year -> Bool  
leapYear year = year `mod` 4 == 0  && not(year `mod` 100 == 0) || year `mod` 400 == 0
  
getWeekday :: Year -> Month -> Weekday
getWeekday y m = (daysSinceStart y m) `mod` 7
  
isSunday :: Year -> Month  -> Bool  
isSunday y m = (getWeekday y m) == 6


daysSinceStart :: Year -> Month -> Int
daysSinceStart 1900 1 = 0
daysSinceStart y 1 = daysSinceStart (y-1) 1 + daysInYear (y-1)
daysSinceStart y m = daysSinceStart y (m-1) + daysInMonth y (m-1)


daysInYear :: Year -> Int
daysInYear y = sum $ map (daysInMonth y) [1..12]

daysInMonth :: Year -> Month ->  Int
daysInMonth _ m  | m `elem` [4,6,9,11] = 30
daysInMonth y 2 | leapYear y = 29
daysInMonth y 2 |otherwise = 28
daysInMonth _   _   = 31


result = length $ filter id [isSunday y m | y <- [1901..2000], m<-[1..12]]