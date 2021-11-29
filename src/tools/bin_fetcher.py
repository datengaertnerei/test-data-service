import csv

import lxml.html as lh
import requests

csvwriter = None

def extract_row_values(row):
    type = ''
    bin = ''
    children = list(row[1])
    if (0 < len(children)):
        type = children[0].text
    children = list(row[2])
    if (0 < len(children)):
        children = list(children[0])
        if (0 < len(children)):
            bin = children[0].text
    if len(type) > 0 and len(bin) > 0:
        csvwriter.writerow([type, bin])


if __name__ == '__main__':
    host = 'https://bincheck.org'
    path = '/germany'  # Create a handle, page, to handle the contents of the website
    page = requests.get(host + path)  # Store the contents of the website under doc
    csvfile = open('binlist.csv', 'w', newline='')
    csvwriter = csv.writer(csvfile, delimiter=';', quoting=csv.QUOTE_NONE)
    while len(path) > 0:
        doc = lh.fromstring(page.content)  # Parse data that are stored between <tr>..</tr> of HTML
        rows = doc.xpath('//tr')
        for row in rows:
            extract_row_values(row)

        next = doc.xpath('//a[@rel="next"]/@href')
        if len(next) > 0:
            path = next[0]
        else:
            path = ''
        page.close()
        page = requests.get(host + path)
