from UserDict import DictMixin
import re

class ClobberedDictKey(Exception):
    "A flag that a variable has been assigned two incompatible values."
    pass

class NoClobberDict(DictMixin):
    """
    A dictionary-like object that prevents its values from being
    overwritten by different values. If that happens, it indicates a
    failure to match.
    """
    def __init__(self, initial_dict = None):
        if initial_dict == None:
            self._dict = {}
        else:
            self._dict = dict(initial_dict)
        
    def __getitem__(self, key):
        return self._dict[key]

    def __setitem__(self, key, value):
        if self._dict.has_key(key) and self._dict[key] != value:
            raise ClobberedDictKey, (key, value)

        self._dict[key] = value

    def __delitem__(self, key):
        del self._dict[key]

    def __contains__(self, key):
        return self._dict.__contains__(key)

    def __iter__(self):
        return self._dict.__iter__()

    def iteritems(self):
        return self._dict.iteritems()
        
    def keys(self):
        return self._dict.keys()

# A regular expression for finding variables.
#Using \w instead of \S so successant groups can be used.
# like "(?a)(?b)
AIRegex = re.compile(r'\(\?(\w+)\)')

def AIStringToRegex(AIStr):

    used_groups = []

    def replace(m):
        groupname = m.group(1)
        if groupname in used_groups:
            return r'(?P=%s)' %(groupname)
        else:
            used_groups.append(groupname)
            return r'(?P<%s>\S+)' %(groupname) 

    re_template = AIRegex.sub(replace , AIStr )+'$'
    return re_template


def AIStringToPyTemplate(AIStr):
    return AIRegex.sub( r'%(\1)s', AIStr )

def AIStringVars(AIStr):
    # This is not the fastest way of doing things, but
    # it is probably the most explicit and robust
    return set([ AIRegex.sub(r'\1', x) for x in AIRegex.findall(AIStr) ])

