from flask import Flask, render_template, request, jsonify
from markupsafe import Markup
from utils.fertilizer import fertilizer_dic
import pandas as pd
import numpy as np
import pickle
import sklearn
import sqlite3
import os


print(sklearn.__version__)
#loading models
dtr = pickle.load(open('dtr.pkl','rb'))
preprocessor = pickle.load(open('preprocessor .pkl','rb'))
model = pickle.load(open('model.pkl','rb'))
sc = pickle.load(open('standscaler.pkl','rb'))
ms = pickle.load(open('minmaxscaler.pkl','rb'))

# flask app
app = Flask(__name__)


@app.route('/index')
def index():
    return render_template('index.html')

@app.route('/index1')
def index1():
    return render_template('index1.html')

@ app.route('/fertilizer')
def fertilizer_recommendation():
    return render_template('fertilizer.html')
@ app.route('/')
def demo():
    return render_template('demo.html')
@ app.route('/Dashboard')
def dashboard():
    return render_template('Dashboard.html')
@ app.route('/StockManagement')
def stock():
    return render_template('stock.html')
@app.route("/predict1", methods=['POST'])
def predict1():
    if request.method == 'POST':
        Year = 2024
        average_rain_fall_mm_per_year = request.form['average_rain_fall_mm_per_year']
        pesticides_tonnes = request.form['pesticides_tonnes']
        avg_temp = request.form['avg_temp']
        Area = request.form['Area']
        Item = request.form['Item']

        features = np.array([[Year, average_rain_fall_mm_per_year, pesticides_tonnes, avg_temp, Area, Item]], dtype=object)
        transformed_features = preprocessor.transform(features)
        prediction = dtr.predict(transformed_features).reshape(1, -1)

        return render_template('index.html', prediction=prediction)

@app.route("/predict2", methods=['POST'])
def predict2():
    N = request.form['Nitrogen']
    P = request.form['Phosporus']
    K = request.form['Potassium']
    temp = request.form['Temperature']
    humidity = request.form['Humidity']
    ph = request.form['Ph']
    rainfall = request.form['Rainfall']

    feature_list = [N, P, K, temp, humidity, ph, rainfall]
    single_pred = np.array(feature_list).reshape(1, -1)

    scaled_features = ms.transform(single_pred)
    final_features = sc.transform(scaled_features)
    prediction = model.predict(final_features)

    crop_dict = {1: "Rice", 2: "Maize", 3: "Jute", 4: "Cotton", 5: "Coconut", 6: "Papaya", 7: "Orange",
                 8: "Apple", 9: "Muskmelon", 10: "Watermelon", 11: "Grapes", 12: "Mango", 13: "Banana",
                 14: "Pomegranate", 15: "Lentil", 16: "Blackgram", 17: "Mungbean", 18: "Mothbeans",
                 19: "Pigeonpeas", 20: "Kidneybeans", 21: "Chickpea", 22: "Coffee"}
    if prediction[0] in crop_dict:
        crop = crop_dict[prediction[0]]
        result = "{} is the best crop to be cultivated right there".format(crop)
    else:
        result = "Sorry, we could not determine the best crop to be cultivated with the provided data."
    return render_template('index1.html', result=result)
@ app.route('/fertilizer-predict', methods=['POST'])
def fert_recommend():
    crop_name = str(request.form['cropname'])
    N = int(request.form['nitrogen'])
    P = int(request.form['phosphorous'])
    K = int(request.form['pottasium'])
    # ph = float(request.form['ph'])

    df = pd.read_csv('fertilizer.csv')

    nr = df[df['Crop'] == crop_name]['N'].iloc[0]
    pr = df[df['Crop'] == crop_name]['P'].iloc[0]
    kr = df[df['Crop'] == crop_name]['K'].iloc[0]

    n = nr - N
    p = pr - P
    k = kr - K
    temp = {abs(n): "N", abs(p): "P", abs(k): "K"}  #ensure values are non-negative
    max_value = temp[max(temp.keys())]
    if max_value == "N":
        if n < 0:
            key = 'NHigh'
        else:
            key = "Nlow"
    elif max_value == "P":
        if p < 0:
            key = 'PHigh'
        else:
            key = "Plow"
    else:
        if k < 0:
            key = 'KHigh'
        else:
            key = "Klow"

    response = Markup(str(fertilizer_dic[key]))

    return render_template('fertilizer.html', recommendation=response)

# stock management, backed by a local sqlite file

STOCK_DB = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'stock.db')


def stock_db():
    conn = sqlite3.connect(STOCK_DB)
    conn.row_factory = sqlite3.Row
    return conn


def init_stock_db():
    with stock_db() as conn:
        conn.execute("""CREATE TABLE IF NOT EXISTS stock (
            product_id TEXT PRIMARY KEY,
            product_name TEXT,
            product_quantity TEXT,
            product_price TEXT)""")


init_stock_db()


@app.route('/api/stock/<pid>', methods=['GET'])
def stock_read(pid):
    with stock_db() as conn:
        row = conn.execute('SELECT * FROM stock WHERE product_id = ?', (pid,)).fetchone()
    if row is None:
        return jsonify({'error': 'not found'}), 404
    return jsonify(dict(row))


@app.route('/api/stock', methods=['POST'])
def stock_insert():
    d = request.get_json(silent=True) or {}
    pid = (d.get('product_id') or '').strip()
    if not pid:
        return jsonify({'error': 'product id is required'}), 400
    with stock_db() as conn:
        conn.execute("""INSERT INTO stock VALUES (?, ?, ?, ?)
                        ON CONFLICT(product_id) DO UPDATE SET
                        product_name = excluded.product_name,
                        product_quantity = excluded.product_quantity,
                        product_price = excluded.product_price""",
                     (pid, d.get('product_name'), d.get('product_quantity'), d.get('product_price')))
    return jsonify({'ok': True})


@app.route('/api/stock/<pid>', methods=['DELETE'])
def stock_delete(pid):
    with stock_db() as conn:
        cur = conn.execute('DELETE FROM stock WHERE product_id = ?', (pid,))
    if cur.rowcount == 0:
        return jsonify({'error': 'not found'}), 404
    return jsonify({'ok': True})


@app.route('/api/stock', methods=['GET'])
def stock_list():
    with stock_db() as conn:
        rows = conn.execute('SELECT * FROM stock ORDER BY product_id').fetchall()
    return jsonify([dict(r) for r in rows])


if __name__ == "__main__":
    app.run(debug=True)