from flask import Flask, jsonify, request
from flask_pymongo import PyMongo

app = Flask(__name__)
app.config["MONGO_URI"] = "mongodb://localhost:27017/SoundNaam"
mongo = PyMongo(app)

# def testcal(money):
#     combination = 1000000 
#     rating = money / combination
#     rating = str(int(rating * 100))
#     print("rating : " + rating)
#     return rating

def calculator(like, dislike):
    combination = like + dislike
    rating = like / combination
    rating = str(int(rating * 100))
    print("rating : " + rating)
    return rating


@app.route('/song/<title>/<artist>', methods = ["GET"])
def CountSongRating(title, artist):
    print("title : " + title)
    print("artist : " + artist)
    databe = mongo.db.Song.find({'title': title, 'artist': artist})
    # print(databe, type(databe))
    for item in databe:
        like = item['like']
        print("like : " + str(like))
        dislike = item['dislike']
        print("dislike : " + str(dislike))
        
    if (dislike == 0 and like == 0):
        return "100 %"
    
    CountRating = calculator(like, dislike)
    # print(CountRating)
    return CountRating + "%"

@app.route('/podcast/<title>/<artist>', methods = ["GET"])
def CountPodcastRating(title, artist):
    print("title : " + title)
    print("artist : " + artist)
    databe = mongo.db.Podcast.find({'title': title, 'artist': artist})
    # print(databe, type(databe))
    for item in databe:
        like = item['like']
        print("like : " + str(like))
        dislike = item['dislike']
        print("dislike : " + str(dislike))
    if (dislike == 0 and like == 0):
        return "100 %"
    CountRating = calculator(like, dislike)
    # print(CountRating)
    return CountRating + "%"

# @app.route('/post')
# def postsong():
#     # databe = mongo.db.Song.find({})
#     # print(databe, type(databe))
#     mongo.db.Song.insert_one({"title" : 'gay', "artist" : 'gaymak', "like": 125, "dislike": 25})
#     mongo.db.Song.insert_one({"title" : 'yai', "artist" : 'yaimak', "like": 50, "dislike": 250})
    
#     # print(CountRating)
#     return "<p>gay and yai<p>"

if __name__ == '__main__':
    app.run(debug=True)