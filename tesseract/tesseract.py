import cv2 
import pytesseract
img = cv2.imread('C:\\Users\\carlo\\Google Drive\\projekte\\pflege-ai\\dokumentation-photos\\IMG_2596.jpg')
result = pytesseract.image_to_string(img, lang='deu')
print(result)